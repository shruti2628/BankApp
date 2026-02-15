export interface Transaction {
  id: number;
  type: string;
  amount: number;
  status: string;
  transactionTime: string;

  accountNumber: string;
  accountBalance: number;

  performedBy: string;
  approvedBy: string | null;
}
