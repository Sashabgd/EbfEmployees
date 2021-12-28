import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-login-dialog',
  templateUrl: './login-dialog.component.html',
  styleUrls: ['./login-dialog.component.css']
})
export class LoginDialogComponent implements OnInit {

  constructor(
    private dialogRef:MatDialogRef<LoginDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data:string
  ) { }

  ngOnInit(): void {
  }

}
