import { Injectable, Pipe } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { HttpClient } from '@angular/common/http';

import { of } from 'rxjs/observable/of';
import { tap, flatMap } from 'rxjs/operators';
import { MessageService } from "../core/message.service";
import { RequestService } from "../core/request.service";

import { APIResponseVoid } from "../model/APIResponse";
import { Organization } from '../model/Organization';



@Injectable()
export class OrganizationService {

  constructor(private http: HttpClient, private messageService: MessageService, private requestService: RequestService) { }

  createOrg(organization: Organization) {
    var initUrl = this.requestService.prefixHost("organization/init");
    var deployUrl = this.requestService.prefixHost("organization/deploy");

    return this.http.post<APIResponseVoid>(initUrl, organization, this.requestService.httpOptions)
      .pipe(
        flatMap(response => {
          if(response.success){
            this.messageService.add(response.message);
            return this.http.post<APIResponseVoid>(deployUrl, organization, this.requestService.httpOptions);
          }
          return of(response as APIResponseVoid);
        }),
        tap(response => {
            this.messageService.add(response.message);
        })
      );
  }
}
