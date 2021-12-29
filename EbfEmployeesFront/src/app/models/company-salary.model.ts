import { CompanyModel } from './company.model';
export class CompanySalaryModel extends CompanyModel {
  constructor(id: number, name: string, public avgSalary: number) {
    super(id, name);
  }
}
