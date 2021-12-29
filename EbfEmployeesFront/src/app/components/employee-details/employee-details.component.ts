import { MatSnackBar } from '@angular/material/snack-bar';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { EmployeeModel } from './../../models/employee.model';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-employee-details',
  templateUrl: './employee-details.component.html',
  styleUrls: ['./employee-details.component.css']
})
export class EmployeeDetailsComponent implements OnInit {

  public employee!: EmployeeModel;
  public id!: number;

  constructor(
    private activatedRoute: ActivatedRoute,
    private httpClient: HttpClient,
    private snackBar: MatSnackBar
  ) {
    this.activatedRoute.params.subscribe(r => {
      if (r["id"]) {
        this.id = r['id'];
      }
    })

  }

  ngOnInit(): void {
    if (this.id) {
      this.httpClient.get<EmployeeModel>(`/api/employees/${this.id}`)
        .subscribe({
          next: (e) => {
            this.employee = e;
          },
          error: (e) => {
            this.snackBar.open(e.message, "OK", { duration: 2000 });
          }
        })
    }
  }
}

