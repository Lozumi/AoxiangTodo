// nuxt项目目录/composables/AccountRequest.ts

let baseUrl = 'http://10.60.50.102:20220'

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

    login(loginData: any) {
        const requestBody = {
            content:JSON.stringify({
                '@class': 'shared.UserInfo',
                userName : '',
                account: loginData.account,
                password: loginData.password
            }),
            requestType: 'UserLogin',
        };
        console.log(requestBody)
        return sendRequest(requestBody);
    }

    register(registerData: any) {
        const requestBody = {
            content:JSON.stringify({
                '@class': 'shared.UserInfo',
                userName : registerData.userName,
                account: registerData.account,
                password: registerData.password
            }),
            requestType: 'UserRegister',
        };
        return sendRequest(requestBody);
    }

}
