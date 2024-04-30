package dev.codescreen;

/*
 * Initial thoughts:
 * - We'll be building a Domain Model to represent the bank ledger system.
 * - We need to create TransactionEvent class to represent the transaction event.
 * - This class will represent 1 transaction event in the transaction log.
 * - The transaction event will have a timestamp, a transaction type (credit or debit), and an amount, allowing for the
 *   reversal of the transaction.
 * - The transaction log will be a list of TransactionEvent objects, and can be used to rebuild the state of the system.
 * - It's possible that I could use the information I learned from the Multi-Threading course to improve the performance
 *   of the system.
 * - We need to implement Snapshots. This could be done by creating a Snapshot class that will store the state of the
 *   system at a given point in time, and will just get updated when a new transaction event is added to the
 *   transaction.
 * - We could also implement a way to run all the TransactionEvents in the transaction log to rebuild the Snapshot on a
 *   regular basis. On second thought, this is not specified, but it is good practice to allow such functionality to be
 *   later implemented easily. For now, TransactionEvents will just be there for audit (a source of truth).
 * - As I continue reading, I will keep adding more thoughts.
 */
public class TransactionEvent {

}
