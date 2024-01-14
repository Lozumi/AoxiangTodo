// nuxt项目目录/composables/ToDoWorkRequest.ts

import {randomUUID} from "uncrypto";

let baseUrl = 'http://localhost:20220'

const sendRequest = (body: any) => {
  return useFetch(baseUrl, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(body),
    immediate:true,
    // getCachedData: (...key)=>{
    //   console.log('try to get cached by key',key,body);
    //   debugger
    //   return undefined
    // }
  });
};

const createTodoWork = (workLayer:number,workInnerId:number,workImportancePriority:number,workEmergencyPriority:number,workTitle:string,workSubtitle:string,workDescription:string,workCreateTime:string,workStartTime:string,workDeadline:string,workStatus:string,workSubToDoWorkItemInnerIdList:any,workPomodoroRecordInnerIdList:any) => {
  return JSON.stringify({
    '@class': 'shared.ToDoWorkItem',
    layer: workLayer,
    innerId: workInnerId,
    importancePriority: workImportancePriority,
    emergencyPriority: workEmergencyPriority,
    title: workTitle,
    subtitle: workSubtitle,
    description: workDescription,
    createTime: workCreateTime,
    startTime: workStartTime,
    deadLine: workDeadline,
    status: workStatus,
    subToDoWorkItemInnerIdList: workSubToDoWorkItemInnerIdList,
    pomodoroRecordInnerIdList: workPomodoroRecordInnerIdList,
  });
}

export default new class ToDoWorkRequest {
  create(todoItem: any) {
    const requestBody = {
      content: todoItem,
      requestType: 'CreateToDoWork',
    };
    return sendRequest(requestBody);
  }

  query(innerId: number) {
    const requestBody = {
      content:innerId,
      requestType: 'QueryToDoWork',
    };
    return sendRequest(requestBody);
  }
  delete(innerId: number) {
    const requestBody = {
      content:innerId,
      requestType: 'DeleteToDoWork',
    };
    return sendRequest(requestBody);
  }

  edit(updatedTodoItem: any) {
    const requestBody = {
      content: updatedTodoItem,
      requestType: 'EditToDoWork',
    };
    return sendRequest(requestBody);
  }

  enumerate() {
    const requestBody = {
      content: '',
      requestType: 'EnumerateToDoWorkList',
    };
    return sendRequest(requestBody);
  }

}
