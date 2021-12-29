import { CompanySalaryModel } from './../../models/company-salary.model';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { CompanyModel } from 'src/app/models/company.model';
import { PageModel } from 'src/app/models/page.model';

@Component({
  selector: 'app-reports',
  templateUrl: './reports.component.html',
  styleUrls: ['./reports.component.css']
})
export class ReportsComponent implements OnInit {

  public tableData!: PageModel<CompanySalaryModel>;
  displayedColumns: string[] = ['id', 'name','avgSalary', 'delete'];

  constructor(private httpClient: HttpClient, private snackBar: MatSnackBar, private router: Router, private dialog: MatDialog) { }

  ngOnInit(): void {
    this.loadCompanies();
  }

  public deleteCompany(company: CompanyModel): void {
    this.httpClient.delete(`/api/companies/${company.id}`).subscribe({
      next: () => {
        this.loadCompanies();
      },
      error: () => {
        this.snackBar.open("Error deleting company", "OK", { duration: 2000 });
      }
    });
  }

  private loadCompanies(): void {
    this.httpClient.get<PageModel<CompanySalaryModel>>('/api/companies/avg-salaries').subscribe(r => {
      this.tableData = r;
    })
  }

  public openCompanyDetail(company: CompanyModel): void {
    this.router.navigate([`/company/${company.id}`])
  }
}
