import { MatSnackBar } from '@angular/material/snack-bar';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { EmployeeModel } from './../../models/employee.model';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-employee-details',
  templateUrl: './employee-details.component.html',
  styleUrls: ['./employee-details.component.css']
})
export class EmployeeDetailsComponent implements OnInit {

  public employee!: EmployeeModel;
  public id!: number;
  public employeeForm!: FormGroup;

  constructor(
    private activatedRoute: ActivatedRoute,
    private httpClient: HttpClient,
    private snackBar: MatSnackBar,
    private fb: FormBuilder
  ) {
    this.activatedRoute.params.subscribe(r => {
      if (r["id"]) {
        this.id = r['id'];
      }
    })

  }

  ngOnInit(): void {

    this.employeeForm = this.fb.group({
      name: ['', Validators.required],
      surname: ['', Validators.required],
      email: ['', Validators.email],
      address: ['', Validators.required],
      salary: ['', Validators.required]
    });

    this.loadEmployee();
  }

  public loadEmployee() {
    if (this.id) {
      this.httpClient.get<EmployeeModel>(`/api/employees/${this.id}`)
        .subscribe({
          next: (e) => {
            this.employee = e;
            this.employeeForm.controls['name'].setValue(e.name);
            this.employeeForm.controls['surname'].setValue(e.surname);
            this.employeeForm.controls['email'].setValue(e.email);
            this.employeeForm.controls['address'].setValue(e.address);
            this.employeeForm.controls['salary'].setValue(e.salary);
          },
          error: (e) => {
            this.snackBar.open(e.error.message, "OK", { duration: 2000 });
          }
        })
    }
  }

  public updateEmployee(): void {
    if (this.employeeForm.invalid) {
      this.snackBar.open("Form is invalid", "OK", { duration: 2000 });
      return;
    }
    this.httpClient.put(`/api/employees/${this.id}`, {
      name: this.employeeForm.controls['name'].value,
      surname: this.employeeForm.controls['surname'].value,
      email: this.employeeForm.controls['email'].value,
      address: this.employeeForm.controls['address'].value,
      salary: this.employeeForm.controls['salary'].value,
      companyId: this.employee.company.id,
    }).subscribe({
      next: () => {
        this.loadEmployee();
      },
      error: (e) => {
        this.snackBar.open(e.error.message, "OK", { duration: 2000 });
      }
    })
  }
}

