import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ReactiveFormsModule } from '@angular/forms';
import { FormlyModule } from '@ngx-formly/core';
import { HttpClientModule } from '@angular/common/http';
import { FormlyBootstrapModule } from '@ngx-formly/bootstrap';

var formlyModule = FormlyModule.forRoot({
    validationMessages: [
        { name: 'required', message: '必填' },
    ]
});

@NgModule({
    imports: [
        CommonModule,
        HttpClientModule,
        ReactiveFormsModule,
        formlyModule,
        FormlyBootstrapModule
    ],
    exports: [
        CommonModule,
        HttpClientModule,
        ReactiveFormsModule,
        FormlyModule,
        FormlyBootstrapModule],
    declarations: [],
    providers: [],
})
export class SharedModule { }
