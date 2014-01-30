read_write_cas
==============

Code for coarse grain and fine grain locking on a circularly doubly linked list, the lock used is the read write lock with fairness. Compare and set mechanism from java is used for making the read write locks

Read Write Lock Algorithm - 

0 locked for write
1 unlocked
-1 unlocked, but giving lock to writer
all other numbers locked for read
positive number N>1  --- indicates locked for read, N-1 = number of readers
negative number N<-1 --- indicates locked for read, but writers are enqueued
 Thus, |N|-1 = number of readers current holding the lock

We will define “fairness” as follows: when many readers hold a lock and a writer attempts to acquire it, no further readers will acquire the lock until that writer is allowed to proceed. The writer, however, still must wait until the current readers, which hold the lock are done and release the lock.  Once the lock is released writers and readers have equal chance to acquire the lock.  



