import { AccessTokenModel } from './../models/access-token.model';
import { LoginDialogComponent } from './../components/login-dialog/login-dialog.component';
import { HttpEvent, HttpHandler, HttpHeaders, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { MatDialog } from "@angular/material/dialog";
import { catchError, filter, Observable, switchMap, take, throwError } from "rxjs";
import { StorageService } from "./storage.service";
import { MatSnackBar } from '@angular/material/snack-bar';
import { DatabaseSelectionService } from './database-selection.service';

@Injectable({ providedIn: 'root' })
export class HttpInterceptorService implements HttpInterceptor {

  constructor(private storageService: StorageService, private matDialog: MatDialog, private snackBar: MatSnackBar, private databaseSelectionService: DatabaseSelectionService) { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(this.cloneRequestWithAccessToken(req)).pipe(catchError(e => {
      if (e.error && e.error.message && e.error.message.length != 0) {
        this.snackBar.open(e.error.message, "OK", { duration: 2000 });
      } else {
        this.snackBar.open(`Error ${e.status}`, "OK", { duration: 2000 });
      }
      if (e.status == 401 || e.status == 403 && req.url != '/api/login') {
        let dialog = this.matDialog.open(LoginDialogComponent, { width: '40rem', data: "dada", panelClass: 'app-dialog' });
        return dialog.afterClosed().pipe(take(1), switchMap(accessToken => {
          if (accessToken == null) {
            return throwError(() => e);
          }
          this.storageService.setAccessToken(accessToken.accessToken);
          return next.handle(this.cloneRequestWithAccessToken(req));
        }));
      } else {
        return throwError(() => e);
      }
    }));
  }

  cloneRequestWithAccessToken(request: HttpRequest<any>): HttpRequest<any> {
    if (this.storageService.getAccessToken() != null) {
      let httph = new HttpHeaders({'Authorization': `Bearer ${this.storageService.getAccessToken()}`});
      if(this.databaseSelectionService.getDatabase() != null){
        let database = this.databaseSelectionService.getDatabase() as string;
        httph = new HttpHeaders({'Authorization': `Bearer ${this.storageService.getAccessToken()}`,
        'Selected-Database': database});
      }
      
      return request.clone(
        {
          withCredentials: true,
          headers: httph
        }
      )
    } else {
      return request;
    }
  }
}
