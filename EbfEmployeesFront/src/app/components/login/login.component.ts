import { HttpClient } from '@angular/common/http';
import { Component, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AccessTokenModel } from 'src/app/models/access-token.model';
import { EventEmitter } from '@angular/core';
import { StorageService } from 'src/app/services/storage.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  @Output("accessTokenEvent")
  public accessTokenEvent: EventEmitter<AccessTokenModel> = new EventEmitter<AccessTokenModel>();

  public loginForm!: FormGroup;

  constructor(private httpClient: HttpClient,
    private fb: FormBuilder,
    private storageService: StorageService,
    private matSnackbar: MatSnackBar) { }


  ngOnInit(): void {

    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  public onLogin(): void {
    this.httpClient.post<AccessTokenModel>('/api/login',
      {
        "username": this.loginForm.controls['username'].value,
        "password": this.loginForm.controls['password'].value
      }).subscribe({
        next : r =>{
          this.storageService.setAccessToken(r.accessToken);
          this.accessTokenEvent.emit(r);
        },
        error : e=>{this.matSnackbar.open("Invalid username or password!","OK",{duration:2000})}
      });
  }
}
