import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { tap } from 'rxjs/operators';

import { MessageService } from "../core/message.service";
import { RequestService } from "../core/request.service";

import { APIResponseVoid } from "../model/APIResponse";


@Injectable()
export class ChannelService {
    constructor(private http: HttpClient, private requestService: RequestService, private messageService: MessageService) {

    }

    createChannel({orgName, channelName }) {
        var url = this.requestService.prefixHost("channel/create");

        return this.http.post<APIResponseVoid>(url, {orgName, channelName }, this.requestService.httpOptions)
            .pipe(
                tap(response => {
                    this.messageService.add(response.message);
                })
            );
    }

    addOrg({adminOrgName, channelName, applicantOrgName }) {
        var url = this.requestService.prefixHost("channel/addorg");
        return this.http.post<APIResponseVoid>(url, { adminOrgName, channelName, applicantOrgName  }, this.requestService.httpOptions)
        .pipe(
            tap(response => {
                this.messageService.add(response.message);
            })
        );
    }
}