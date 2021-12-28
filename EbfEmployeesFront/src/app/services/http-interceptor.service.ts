import { AccessTokenModel } from './../models/access-token.model';
import { LoginDialogComponent } from './../components/login-dialog/login-dialog.component';
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { MatDialog } from "@angular/material/dialog";
import { catchError, filter, Observable, switchMap, take, throwError } from "rxjs";
import { StorageService } from "./storage.service";

@Injectable({ providedIn: 'root' })
export class HttpInterceptorService implements HttpInterceptor {

  constructor(private storageService: StorageService, private matDialog: MatDialog) { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(this.cloneRequestWithAccessToken(req)).pipe(catchError(e => {
      if (e.status == 401 || e.status == 403) {
        let dialog = this.matDialog.open(LoginDialogComponent, { width: '40rem', data: "dada", panelClass: 'app-dialog' });
        return dialog.afterClosed().pipe(filter(t => t != null), take(1), switchMap(accessToken => {
          this.storageService.setAccessToken(accessToken.accessToken);
          return next.handle(this.cloneRequestWithAccessToken(req));
        }));
      } else {
        return throwError(() => e);
      }
    }));
  }

  cloneRequestWithAccessToken(request: HttpRequest<any>): HttpRequest<any> {
    return request.clone(
      {
        withCredentials: true,
        setHeaders: {
          Authorization: `Bearer ${this.storageService.getAccessToken()}`
        }
      }
    )
  }
}
