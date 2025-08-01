import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-tecnicos',
  templateUrl: './tecnicos.html',
  styleUrl: './tecnicos.css',
  standalone: true,
  imports: [HttpClient]
})
export class Tecnicos {
  tecnicos: any[] = [];

  constructor(private http: HttpClient) {
    this.http.get<any[]>('http://localhost:80080/tecnicos')
      .subscribe(data => this.tecnicos = data);
  }
}
