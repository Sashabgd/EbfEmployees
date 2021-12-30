import { PageModel } from './../../models/page.model';
import { EmployeeModel } from './../../models/employee.model';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CompanySalaryModel } from './../../models/company-salary.model';
import { HttpClient } from '@angular/common/http';
import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { CreateEmployeeDialogComponent } from '../dialogs/create-employee-dialog/create-employee-dialog.component';
import { PageEvent } from '@angular/material/paginator';

@Component({
  selector: 'app-company-details',
  templateUrl: './company-details.component.html',
  styleUrls: ['./company-details.component.css']
})
export class CompanyDetailsComponent implements OnInit {

  @Input("id")
  public id!: number;

  displayedColumns: string[] = ['id', 'name', 'surname', 'email', 'address', 'company', 'salary', 'delete'];
  private size = 10;
  private index = 0;

  public company!: CompanySalaryModel;
  public employees!: PageModel<EmployeeModel>;

  constructor(private httpClient: HttpClient,
    private snackbar: MatSnackBar,
    private activatedRoute: ActivatedRoute,
    private dialog: MatDialog,
    private route: Router) {
    this.activatedRoute.params.subscribe(p => {
      if (p['id']) {
        this.id = p['id'];
      }
    })

  }

  ngOnInit(): void {
    this.loadCompanyDetails();
  }

  private loadEmployees() {
    this.httpClient.get<PageModel<EmployeeModel>>(`/api/companies/${this.id}/employees?size=${this.size}&page=${this.index}`)
      .subscribe({
        next: (employees) => {
          this.employees = employees;
        },
        error: (e) => {
          this.snackbar.open(e.error.message, "OK", { duration: 2000 });
        }
      })
  }

  private loadCompanyDetails(): void {
    this.httpClient.get<CompanySalaryModel>(`/api/companies/${this.id}/avg-salary`)
      .subscribe({
        next: (response) => {
          this.company = response;
          this.loadEmployees();
        },
        error: (error) => {
          this.snackbar.open(error.error.message, "OK", { duration: 2000 });
        }
      })
  }

  public createEmployee() {
    let dref = this.dialog.open(CreateEmployeeDialogComponent, {
      data: this.company,
      width: '500px',
      panelClass: 'app-dialog'
    });
    dref.afterClosed().subscribe(r => {
      if (r) {
        this.loadCompanyDetails();
      }
    })
  }

  public deleteEmployee(employee: EmployeeModel): void {
    this.httpClient.delete(`/api/employees/${employee.id}`)
      .subscribe({
        next: () => {
          this.loadCompanyDetails();
        },
        error: (e) => {
          this.snackbar.open(e.error.message, "OK", { duration: 2000 });
        }
      })
  }

  public openEmployeeDetail(employee: EmployeeModel) {
    this.route.navigate([`/employee/${employee.id}`]);
  }

  public generateEmployees(): void {
    this.httpClient.post(`/api/companies/generate/${this.id}/employees`, null).subscribe({
      next: () => {
        this.loadCompanyDetails();
      },
      error: e => {
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
