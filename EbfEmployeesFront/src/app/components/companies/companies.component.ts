import { PageModel } from './../../models/page.model';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { CompanyModel } from 'src/app/models/company.model';

@Component({
  selector: 'app-companies',
  templateUrl: './companies.component.html',
  styleUrls: ['./companies.component.css']
})
export class CompaniesComponent implements OnInit {

  public tableData!:PageModel<CompanyModel>;
  displayedColumns: string[] = ['id', 'name'];

  constructor(private httpClient: HttpClient) { }

  ngOnInit(): void {
    this.httpClient.get<PageModel<CompanyModel>>('/api/companies').subscribe(r => {
      this.tableData = r;
    })
  }

}
