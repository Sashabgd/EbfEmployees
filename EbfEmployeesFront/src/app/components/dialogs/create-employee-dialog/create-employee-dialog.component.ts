import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { HttpClient } from '@angular/common/http';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Component, Inject, OnInit } from '@angular/core';
import { CompanyModel } from 'src/app/models/company.model';

@Component({
  selector: 'app-create-employee-dialog',
  templateUrl: './create-employee-dialog.component.html',
  styleUrls: ['./create-employee-dialog.component.css']
})
export class CreateEmployeeDialogComponent implements OnInit {

  public createEmployeeForm!: FormGroup;

  constructor(
    private dialogRef: MatDialogRef<CreateEmployeeDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: CompanyModel,
    private httpClient: HttpClient,
    private snackBar: MatSnackBar,
    private fb: FormBuilder
  ) { }

  ngOnInit(): void {
    this.createEmployeeForm = this.fb.group({
      name: ['', Validators.required],
      surname: ['', Validators.required],
      email: ['', Validators.email],
      address: ['', Validators.required],
      salary: ['', Validators.required]
    });
  }

  public createEmployee() {
    if (this.createEmployeeForm.valid) {
      this.httpClient.post('/api/employees/',
        {
          name: this.createEmployeeForm.controls['name'].value,
          surname: this.createEmployeeForm.controls['surname'].value,
          email: this.createEmployeeForm.controls['email'].value,
          address: this.createEmployeeForm.controls['address'].value,
          salary: this.createEmployeeForm.controls['salary'].value,
          companyId: this.data.id,
        }).subscribe({
          next: () => {
            this.dialogRef.close(true);
          },
          error: (e) => {
            this.snackBar.open(e.message, "OK", { duration: 2000 });
          }
        })
    } else {
      this.snackBar.open("Form is not valid", "OK", { duration: 2000 });
    }
  }

}
