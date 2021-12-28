import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { StorageService } from "./storage.service";

@Injectable({ providedIn: 'root' })
export class HttpInterceptorService implements HttpInterceptor {

  constructor(private storageService: StorageService) { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(this.cloneRequestWithAccessToken(req));
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
