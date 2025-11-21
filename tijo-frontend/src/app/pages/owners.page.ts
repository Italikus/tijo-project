import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OwnerService } from '../services/owner.service';
import { CarService } from '../services/car.service';

@Component({
  selector: 'app-owners',
  standalone: true,
  imports: [CommonModule],
  template: `
    <h2>Owners</h2>
    <form (submit)="onCreate($event)" class="form-inline">
      <input name="name" placeholder="Name" required />
      <input name="surname" placeholder="Surname" required />
      <input name="pesel" placeholder="PESEL" required />
      <button type="submit">Create Owner</button>
    </form>

    <h3>Assign car to owner</h3>
    <form (submit)="onAssign($event)" class="form-inline">
      <input name="ownerId" placeholder="Owner ID" type="number" required />
      <input name="carId" placeholder="Car ID" type="number" required />
      <button type="submit">Assign</button>
    </form>

    <table class="data-table" *ngIf="owners().length > 0">
      <thead>
        <tr>
          <th>ID</th>
          <th>Name</th>
          <th>Surname</th>
          <th>PESEL</th>
          <th>Owned Cars</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        <tr *ngFor="let o of owners()">
          <td>{{ o.id }}</td>
          <td>{{ o.name }}</td>
          <td>{{ o.surname }}</td>
          <td>{{ o.pesel }}</td>
          <td>{{ o.ownedCars }}</td>
          <td>
            <button (click)="delete(o.id)">Delete</button>
          </td>
        </tr>
      </tbody>
    </table>

    <p *ngIf="owners().length === 0">No owners available.</p>
  `
})
export class OwnersPage {
  owners = signal<any[]>([]);
  cars = signal<any[]>([]);

  constructor(private ownerService: OwnerService, private carService: CarService) {
    this.reload();
  }

  reload() {
    this.ownerService.list().subscribe({
      next: (res: any) => this.owners.set(res.content ?? res),
      error: () => {}
    });
    this.carService.list().subscribe({
      next: (res: any) => this.cars.set(res.content ?? res),
      error: () => {}
    });
  }

  onCreate(e: Event) {
    e.preventDefault();
    const form = e.target as HTMLFormElement;
    const data = new FormData(form);
    const command: any = {
      name: data.get('name'),
      surname: data.get('surname'),
      pesel: data.get('pesel')
    };
    this.ownerService.create(command).subscribe({
      next: () => { this.reload(); form.reset(); },
      error: () => { /* nie czyścimy formularza na błąd walidacji */ }
    });
  }

  onAssign(e: Event) {
    e.preventDefault();
    const form = e.target as HTMLFormElement;
    const data = new FormData(form);
    const ownerId = Number(data.get('ownerId'));
    const carId = Number(data.get('carId'));
    this.ownerService.assignCar(ownerId, carId).subscribe({
      next: () => { this.reload(); form.reset(); },
      error: () => {}
    });
  }

  delete(id: number) {
    this.ownerService.delete(id).subscribe({
      next: () => this.reload(),
      error: () => {}
    });
  }
}
