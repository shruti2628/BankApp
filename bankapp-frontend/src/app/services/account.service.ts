import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class AccountService {

  private baseUrl = 'http://localhost:8080/manager/accounts';

  constructor(private http: HttpClient) {}

  createAccount(accountData: any) {

    const token = localStorage.getItem('token');

    const headers = {
      Authorization: `Bearer ${token}`
    };

    return this.http.post(
      `${this.baseUrl}/create`,
      accountData,
      { headers }
    );
  }
}
