import { Injectable } from "@angular/core";

@Injectable({ providedIn: 'root' })
export class StorageService {
  setAccessToken(accessToken: string) {
    sessionStorage.setItem('ebf-access-token', accessToken);
  }

  public getAccessToken(): string | null {
    return sessionStorage.getItem('ebf-access-token');
  }

}
