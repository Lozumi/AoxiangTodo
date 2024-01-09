// nuxt项目目录/composables/AccountRequest.ts

let baseUrl = 'http://localhost:20220'

const sendRequest = (body: any) => {
    return useFetch(baseUrl, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(body),
    });
};


export default new class AccountRequest {

    login(account: string,password: string) {
        const requestBody = {
            content:JSON.stringify({
                '@class': 'shared.UserInfo',
                userName : '',
                account: account,
                password: password
            }),
            requestType: 'UserLogin',
        };
        return sendRequest(requestBody);
    }

    register(userName: string,account: string,password: string) {
        const requestBody = {
            content:JSON.stringify({
                '@class': 'shared.UserInfo',
                userName : userName,
                account: account,
                password: password
            }),
            requestType: 'UserRegister',
        };
        return sendRequest(requestBody);
    }

}
