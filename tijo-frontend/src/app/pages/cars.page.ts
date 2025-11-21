import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CarService } from '../services/car.service';

@Component({
  selector: 'app-cars',
  standalone: true,
  imports: [CommonModule],
  template: `
    <h2>Cars</h2>
    <form (submit)="onCreate($event)" class="form-inline">
      <input name="brand" placeholder="Brand" required />
      <input name="model" placeholder="Model" required />
      <input name="vin" placeholder="VIN" required />
      <input name="horsePower" placeholder="Horse Power" type="number" required />
      <input name="productionYear" placeholder="Production Year" type="number" required />
      <button type="submit">Create Car</button>
    </form>

    <table class="data-table" *ngIf="cars().length > 0">
      <thead>
        <tr>
          <th>ID</th>
          <th>Brand</th>
          <th>Model</th>
          <th>VIN</th>
          <th>Horse Power</th>
          <th>Year</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        <tr *ngFor="let car of cars()">
          <td>{{ car.id }}</td>
          <td>{{ car.brand }}</td>
          <td>{{ car.model }}</td>
          <td>{{ car.vin }}</td>
          <td>{{ car.horsePower }}</td>
          <td>{{ car.year }}</td>
          <td><button (click)="delete(car.id)">Delete</button></td>
        </tr>
      </tbody>
    </table>

    <p *ngIf="cars().length === 0">No cars available.</p>
  `
})
export class CarsPage {
  cars = signal<any[]>([]);

  constructor(private carService: CarService) {
    this.reload();
  }

  reload() {
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
      brand: data.get('brand'),
      model: data.get('model'),
      vin: data.get('vin'),
      horsePower: Number(data.get('horsePower')),
      productionYear: Number(data.get('productionYear'))
    };

    this.carService.create(command).subscribe({
      next: () => { this.reload(); form.reset(); },
      error: () => {}
    });
  }

  delete(id: number) {
    this.carService.delete(id).subscribe({
      next: () => this.reload(),
      error: () => {}
    });
  }
}
