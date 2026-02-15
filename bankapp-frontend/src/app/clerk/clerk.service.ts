import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ClerkService {

  private baseUrl = 'http://localhost:8080/clerk/transactions';

  constructor(private http: HttpClient) {}

  deposit(accountNumber: string, amount: number): Observable<any> {
  const token = localStorage.getItem('token');

  return this.http.post(
    `${this.baseUrl}/deposit?accountNumber=${accountNumber}&amount=${amount}`,
    {}, // empty body
    {
      headers: {
        Authorization: `Bearer ${token}`
      }
    }
  );
}


  withdraw(accountNumber: string, amount: number): Observable<any> {
    const token = localStorage.getItem('token');
    return this.http.post(
      `${this.baseUrl}/withdraw?accountNumber=${accountNumber}&amount=${amount}`,
      {},
      { headers: {
        Authorization: `Bearer ${token}`
      } }
    );
  }
//   getTransactions(accountNumber: string, page: number, size: number) {
//   return this.http.get<any>(
//     `${this.baseUrl}/account/${accountNumber}?page=${page}&size=${size}`
//   );
// }
  getTransactions(accountNumber: string, page: number, size: number) {

  const token = localStorage.getItem('token');  // or whatever key you use

  const headers = {
    Authorization: `Bearer ${token}`
  };

  return this.http.get<any>(
    `${this.baseUrl}/account/${accountNumber}?page=${page}&size=${size}`,
    { headers }
  );
}


}
