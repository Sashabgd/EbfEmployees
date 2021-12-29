import { CreateCompanyDialogComponent } from './../dialogs/create-company-dialog/create-company-dialog.component';
import { MatSnackBar } from '@angular/material/snack-bar';
import { PageModel } from './../../models/page.model';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { CompanyModel } from 'src/app/models/company.model';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { pipe } from 'rxjs';

@Component({
  selector: 'app-companies',
  templateUrl: './companies.component.html',
  styleUrls: ['./companies.component.css']
})
export class CompaniesComponent implements OnInit {

  public tableData!: PageModel<CompanyModel>;
  displayedColumns: string[] = ['id', 'name', 'delete'];

  constructor(private httpClient: HttpClient, private snackBar: MatSnackBar, private router: Router, private dialog: MatDialog) { }

  ngOnInit(): void {
    this.loadCompanies();
  }

  public deleteCompany(company: CompanyModel): void {
    this.httpClient.delete(`/api/companies/${company.id}`).subscribe({
      next: () => {
        this.loadCompanies();
      },
      error: (e) => {
        this.snackBar.open("Error deleting company", "OK", { duration: 2000 });
      }
    });
  }

  private loadCompanies(): void {
    this.httpClient.get<PageModel<CompanyModel>>('/api/companies').subscribe(r => {
      this.tableData = r;
    })
  }

  public openCompanyDetail(company: CompanyModel): void {
    this.router.navigate([`/company/${company.id}`])
  }

  public createCompany(): void {
    let dref = this.dialog.open(CreateCompanyDialogComponent, {
      width: '500px',
      panelClass: 'app-dialog'
    });
    dref.afterClosed().subscribe(r => {
      if (r) {
        this.loadCompanies();
      }
    })
  }

  public generateCompanies():void{
    this.httpClient.post("/api/companies/generate",null).subscribe({
      next:()=>{
        this.loadCompanies();
      }
    })
  }
}
