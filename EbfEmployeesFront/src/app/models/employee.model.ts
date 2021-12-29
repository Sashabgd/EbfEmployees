import { CompanyModel } from './company.model';
export class EmployeeModel {
  constructor(
    public id: number,
    public name: string,
    public surname: string,
    public email: string,
    public address: string,
    public salary: number,
    public company: CompanyModel
  ) { }
}
