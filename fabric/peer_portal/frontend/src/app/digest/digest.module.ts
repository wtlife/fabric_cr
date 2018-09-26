import { NgModule } from '@angular/core';
import { SharedModule } from "../shared/shared.module";
import { InitComponent } from './init/init.component';


@NgModule({
    imports: [SharedModule],
    exports: [],
    declarations: [InitComponent],
    providers: [],
})
export class DigestModule { }
