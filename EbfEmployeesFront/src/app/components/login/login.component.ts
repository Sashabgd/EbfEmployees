import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  constructor(private httpClient: HttpClient) { }

  ngOnInit(): void {
    this.httpClient.post('/api/login', { "username": "admin", "password": "adminpass" }).subscribe(r => {
      console.log(r);
    })
  }

}
