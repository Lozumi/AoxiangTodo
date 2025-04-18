// nuxt项目目录/composables/AccountRequest.ts

export let baseUrl = 'http://localhost:20220'

const useSendRequest = (body: any) => {
    return useFetch(baseUrl, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(body),

    });
};


export default new class AccountRequest {
    useLogin(account: string, password: string) {
        const requestBody = {
            content: JSON.stringify({
                '@class': 'shared.UserInfo',
                userName: '',
                account: account,
                password: password
            }),
            requestType: 'UserLogin',
        };
        return useSendRequest(requestBody);
    }

    useRegister(userName: string, account: string, password: string) {
        const requestBody = {
            content: JSON.stringify({
                '@class': 'shared.UserInfo',
                userName: userName,
                account: account,
                password: password
            }),
            requestType: 'UserRegister',
        };
        return useSendRequest(requestBody);
    }

    userLogout(){
        const requestBody = {
            content: JSON.stringify(''),
            requestType: 'UserLogout',
        };
        return useSendRequest(requestBody);
    }

    getCurrentUser(){
        const requestBody = {
            content: JSON.stringify(''),
            requestType: 'GetCurrentUser',
        };
        return useSendRequest(requestBody);
    }

}
