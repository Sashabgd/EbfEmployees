import { MatSnackBar } from '@angular/material/snack-bar';
import { HttpClient } from '@angular/common/http';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-create-company-dialog',
  templateUrl: './create-company-dialog.component.html',
  styleUrls: ['./create-company-dialog.component.css']
})
export class CreateCompanyDialogComponent implements OnInit {

  public createCompanyForm!: FormGroup;

  constructor(private dialogRef: MatDialogRef<CreateCompanyDialogComponent>,
    private fb: FormBuilder,
    private httpClient: HttpClient,
    private snackbar: MatSnackBar) { }


  ngOnInit(): void {
    this.createCompanyForm = this.fb.group({
      name: ['', Validators.required]
    })
  }

  public createCompany(): void {
    if (this.createCompanyForm.valid) {
      this.httpClient.post('/api/companies/', { name: this.createCompanyForm.controls['name'].value }).subscribe(
        {
          next : ()=>{
            this.dialogRef.close(true);
          },
          error:(e)=>{
            this.snackbar.open(e.message,"OK",{duration:2000});
          }
        }
      )
    } else {
      this.snackbar.open("Company name is required","OK",{duration:2000});
    }
  }
}
