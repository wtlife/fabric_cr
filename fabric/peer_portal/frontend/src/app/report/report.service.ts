import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { RequestService } from "../core/request.service";
import { SummaryInfo } from '../model/SummaryInfo';
import { map } from 'rxjs/operators';
import { APIResponse } from '../model/APIResponse';


@Injectable()
export class ReportService {

  constructor(private http: HttpClient, private requestService: RequestService) { 

   }

   querySummary(){
    var url = this.requestService.prefixHost("report/summary");

    return this.http.get<APIResponse<SummaryInfo[]>>(url, this.requestService.httpOptions).pipe(
      map(response=>{
        if(response.success)
        {
          return response.result;
        }
        else{
          return [];
        }
      })
    );
   }

}
