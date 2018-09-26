import { Component, OnInit } from '@angular/core';
import { ReportService } from '../report.service';
import { SummaryInfo } from '../../model/SummaryInfo';
import { Observable } from 'rxjs/Observable';

@Component({
  selector: 'app-summary',
  templateUrl: './summary.component.html',
  styleUrls: ['./summary.component.css']
})
export class SummaryComponent implements OnInit {

  private summaries: Observable<SummaryInfo[]>;

  constructor(private reportService: ReportService) { }

  ngOnInit() {
    this.summaries = this.reportService.querySummary();
  }

}
