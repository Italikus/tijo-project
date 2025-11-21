import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class CarService {
  private base = 'http://localhost:8080/api/v1/cars';
  constructor(private http: HttpClient) {}

  list(): Observable<any> {
    return this.http.get(this.base);
  }

  create(command: any): Observable<any> {
    return this.http.post(this.base, command);
  }

  delete(id: number): Observable<any> {
    return this.http.delete(`${this.base}/${id}`);
  }
}

