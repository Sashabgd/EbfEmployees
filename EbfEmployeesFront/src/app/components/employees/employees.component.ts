import { MatSnackBar } from '@angular/material/snack-bar';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { PageModel } from './../../models/page.model';
import { Component, OnInit } from '@angular/core';
import { EmployeeModel } from 'src/app/models/employee.model';
import { PageEvent } from '@angular/material/paginator';

@Component({
  selector: 'app-employees',
  templateUrl: './employees.component.html',
  styleUrls: ['./employees.component.css']
})
export class EmployeesComponent implements OnInit {

  public employees!: PageModel<EmployeeModel>;
  displayedColumns: string[] = ['id', 'name', 'surname', 'email', 'address', 'company', 'salary', 'delete'];
  private size = 10;
  private index = 0;

  constructor(
    private route: Router,
    private httpClient: HttpClient,
    private snackbar: MatSnackBar
  ) { }

  ngOnInit(): void {
    this.loadEmployees();
  }

  public deleteEmployee(employee: EmployeeModel): void {
    this.httpClient.delete(`/api/employees/${employee.id}`)
      .subscribe({
        next: () => {
          this.loadEmployees();
        },
        error: (e) => {
          this.snackbar.open(e.error.message, "OK", { duration: 2000 });
        }
      })
  }

  public openEmployeeDetail(employee: EmployeeModel): void {
    this.route.navigate([`/employee/${employee.id}`]);
  }

  private loadEmployees() {
    this.httpClient.get<PageModel<EmployeeModel>>(`/api/employees?size=${this.size}&page=${this.index}`)
      .subscribe({
        next: (employees) => {
          this.employees = employees;
        },
        error: (e) => {
          this.snackbar.open(e.error.message, "OK", { duration: 2000 });
        }
      })
  }

  public pageEvent(pageEvent: PageEvent) {
    this.size = pageEvent.pageSize;
    this.index = pageEvent.pageIndex;
    this.loadEmployees();
  }
}
