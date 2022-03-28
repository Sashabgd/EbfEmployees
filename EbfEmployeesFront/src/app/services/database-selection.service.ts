import { Injectable } from "@angular/core";

@Injectable({ providedIn: 'root' })
export class DatabaseSelectionService {
    public setDatabase(database: string): void {
        sessionStorage.setItem('database', database)
    }

    public getDatabase(): string | null {
        return sessionStorage.getItem('database');
    }
}