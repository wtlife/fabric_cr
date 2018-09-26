import { Injectable } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';

const HTTP_OPTIONS = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};


@Injectable()
export class RequestService {

  prefixHost(path: string): string{
    var host= location.hostname;
    return `http://${host}:8001/${path}`;
  }

  get httpOptions(){
    return HTTP_OPTIONS;
  }
}
