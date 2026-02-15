import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Transaction } from '../models/transaction.model';

@Injectable({
  providedIn: 'root'
})
export class ManagerService {

  private baseUrl = 'http://localhost:8080/manager/transactions';

  constructor(private http: HttpClient) {}

  getPending(): Observable<Transaction[]> {
    const token = localStorage.getItem('token');
    return this.http.get<Transaction[]>(`${this.baseUrl}/pending`,
      {
        headers:{Authorization: `Bearer ${token}`
        }
      }
    );
  }

  approve(id: number) {
    const token = localStorage.getItem('token');
    console.log(token)
    return this.http.post(`${this.baseUrl}/${id}/approve`, 
      {},
      {
          headers:{Authorization: `Bearer ${token}`
        }
      }
    );
  }

  reject(id: number) {
    const token = localStorage.getItem('token');
    return this.http.post(`${this.baseUrl}/${id}/reject`, 
      {},
      {
        headers:{Authorization: `Bearer ${token}`
      }
    }
  );
  }
//   createAccount(account: any) {
//   return this.http.post('http://localhost:8080/manager/accounts', account);
// }
  createAccount(accountData: any) {

    const token = localStorage.getItem('token');

    const headers = {
      Authorization: `Bearer ${token}`
    };

    return this.http.post(
      `http://localhost:8080/manager/accounts/create`,
      accountData,
      { headers }
    );
  }

  getAllAccounts() {

  const token = localStorage.getItem('token');

  const headers = {
    Authorization: `Bearer ${token}`
  };

  return this.http.get<any[]>(
    `http://localhost:8080/manager/accounts/all`,
    { headers }
  );
}

}
