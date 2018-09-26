import { NgModule } from '@angular/core';
import { SharedModule } from "../shared/shared.module";

import { SummaryComponent } from './summary/summary.component';
import { ReportService } from './report.service';


@NgModule({
    imports: [SharedModule],
    exports: [],
    declarations: [SummaryComponent],
    providers: [ReportService],
})
export class ReportModule { }
