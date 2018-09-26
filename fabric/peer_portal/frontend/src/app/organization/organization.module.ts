import { NgModule } from '@angular/core';
import { SharedModule } from "../shared/shared.module";

import { CreateOrgComponent } from './create-org/create-org.component'
import { OrganizationService } from "./organization.service";

@NgModule({
    imports: [SharedModule],
    exports: [],
    declarations: [CreateOrgComponent],
    providers: [OrganizationService],
})
export class OrganizationModule { }
