# Task-Scheduler-Application
Initial phase Development of Task Scheduler using Greedy And Genetic Metaheuristic approach Algorithms
:THe SYstem applies a dual-hybrid mechanism for Scheduling arcihtecture combining low-latency, event-drivn greedy algo with periodic genetic algorithm to optimize the job priority constraints across servesers of cloud computing service.
Helps prevent
:Resource Starvation :Prevent Out Of Memory problem in the OS kernal to terminate the running tasks
:Maximize Efficient Hardware Utilization 
THe WHole Blueprint of execution(Made by AI)
  +-------------------+
                     |   Client HTTP     |
                     +---------+---------+
                               |
                        POST /jobs (202)
                               v
                     +-------------------+
                     |  JobService (DB)  |
                     +---------+---------+
                               | (Publish after commit)
                               v
                    +--------------------+
                    |  RabbitMQ Queue    |
                    +---------+----------+
                              |
                     Consumer Listener
                              v
                 +--------------------------+
                 |  JobSchedulingService    |
                 +------------+-------------+
                              |
       +----------------------+----------------------+
       |                                             |
[Worker Has Capacity]                      [No Capacity / DLQ]
       |                                             |
       v                                             v
Greedy First-Fit                              Job stays PENDING
       |                                             |
       v                                             v
Assign Worker & Log                         GA Batch Scheduler
                                                     |
                                                     v
                                          Multi-Job Matrix Plan
                                                     |
                                                     v
                                            Assign Worker & Log
Note:THe work of AI has been significant through the project from project file structure and providing base code & logic for Greedy and Genetic ALgorithm with logic restrictively to provide base to the mission of developing the project
