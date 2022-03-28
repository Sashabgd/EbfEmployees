import { Component } from '@angular/core';
import { DatabaseSelectionService } from './services/database-selection.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'EbfEmployeesFront';

  constructor(private databaseSelectionService: DatabaseSelectionService){}

  public selectDatabase(database: string): void {
    this.databaseSelectionService.setDatabase(database)
  }
}

