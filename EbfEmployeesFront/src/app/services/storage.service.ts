import { Injectable } from "@angular/core";

@Injectable({ providedIn: 'root' })
export class StorageService {

  public getAccessToken(): string | null {
    return sessionStorage.getItem('ebf-access-token');
  }

}
