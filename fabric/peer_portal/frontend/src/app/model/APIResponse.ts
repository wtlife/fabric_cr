export interface APIResponse<T> {
    success: boolean;
    message: string;
    result: T
}

export interface APIResponseVoid extends APIResponse<void> {

}
