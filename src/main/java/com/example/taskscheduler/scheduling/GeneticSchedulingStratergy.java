package com.example.taskscheduler.scheduling;

import com.example.taskscheduler.domain.Job;
import com.example.taskscheduler.domain.Worker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class GeneticSchedulingStratergy implements BatchSchedulingStratergy {

    private static final double CAPACITY_PENALTY_WEIGHT = 1000.0;
    private static final double IMBALANCE_WEIGHT = 1.0;
    private static final double PRIORITY_WEIGHT = 2.0;

    private final int populationSize;
    private final int generations;
    private final double mutationRate;
    private final double crossoverRate;
    private final int tournamentSize;
    private final Random random = new Random();

    public GeneticSchedulingStratergy(
            @Value("${scheduler.genetic.population-size:60}") int populationSize,
            @Value("${scheduler.genetic.generations:150}") int generations,
            @Value("${scheduler.genetic.mutation-rate:0.05}") double mutationRate,
            @Value("${scheduler.genetic.crossover-rate:0.8}") double crossoverRate,
            @Value("${scheduler.genetic.tournament-size:3}") int tournamentSize) {
        this.populationSize = populationSize;
        this.generations = generations;
        this.mutationRate = mutationRate;
        this.crossoverRate = crossoverRate;
        this.tournamentSize = tournamentSize;
    }

    @Override
    public List<JobAssignmentPlan> plan(List<Job> jobs, List<Worker> workers) {
        if (jobs.isEmpty() || workers.isEmpty()) return List.of();

        int numJobs = jobs.size();
        int numWorkers = workers.size();

        int[][] population = initializePopulation(numJobs, numWorkers);
        int[] best = population[0];
        double bestFitness = fitness(best, jobs, workers);

        for (int gen = 0; gen < generations; gen++) {
            int[][] nextGeneration = new int[populationSize][];
            nextGeneration[0] = best.clone();   // elitism

            for (int i = 1; i < populationSize; i++) {
                int[] parentA = tournamentSelect(population, jobs, workers);
                int[] parentB = tournamentSelect(population, jobs, workers);
                int[] child = (random.nextDouble() < crossoverRate)
                        ? crossover(parentA, parentB) : parentA.clone();
                mutate(child, numWorkers);
                nextGeneration[i] = child;
            }

            population = nextGeneration;
            for (int[] chromosome : population) {
                double f = fitness(chromosome, jobs, workers);
                if (f > bestFitness) {
                    bestFitness = f;
                    best = chromosome;
                }
            }
        }
        return toPlan(best, jobs, workers);
    }

    @Override
    public String name() {
        return "genetic-algorithm";
    }

    private int[][] initializePopulation(int numJobs, int numWorkers) {
        int[][] population = new int[populationSize][numJobs];
        for (int i = 0; i < populationSize; i++) {
            for (int j = 0; j < numJobs; j++) {
                population[i][j] = random.nextInt(numWorkers);
            }
        }
        return population;
    }

    private int[] tournamentSelect(int[][] population, List<Job> jobs, List<Worker> workers) {
        int[] best = population[random.nextInt(population.length)];
        double bestFitness = fitness(best, jobs, workers);
        for (int i = 1; i < tournamentSize; i++) {
            int[] contender = population[random.nextInt(population.length)];
            double contenderFitness = fitness(contender, jobs, workers);
            if (contenderFitness > bestFitness) {
                best = contender;
                bestFitness = contenderFitness;
            }
        }
        return best;
    }

    private int[] crossover(int[] parentA, int[] parentB) {
        int length = parentA.length;
        if (length < 2) return parentA.clone();
        int point = 1 + random.nextInt(length - 1);
        int[] child = new int[length];
        System.arraycopy(parentA, 0, child, 0, point);
        System.arraycopy(parentB, point, child, point, length - point);
        return child;
    }

    private void mutate(int[] chromosome, int numWorkers) {
        for (int i = 0; i < chromosome.length; i++) {
            if (random.nextDouble() < mutationRate) {
                chromosome[i] = random.nextInt(numWorkers);
            }
        }
    }

    private double fitness(int[] chromosome, List<Job> jobs, List<Worker> workers) {
        int numWorkers = workers.size();
        int[] assignedCount = new int[numWorkers];
        for (int gene : chromosome) {
            assignedCount[gene]++;
        }

        double capacityPenalty = 0;
        int[] projectedLoad = new int[numWorkers];
        for (int w = 0; w < numWorkers; w++) {
            projectedLoad[w] = workers.get(w).getCurrentLoad() + assignedCount[w];
            int overflow = projectedLoad[w] - workers.get(w).getCapacity();
            if (overflow > 0) {
                capacityPenalty += overflow * CAPACITY_PENALTY_WEIGHT;
            }
        }

        double mean = 0;
        for (int load : projectedLoad) mean += load;
        mean /= numWorkers;
        double variance = 0;
        for (int load : projectedLoad) {
            variance += (load - mean) * (load - mean);
        }
        variance /= numWorkers;

        double priorityPenalty = 0;
        for (int i = 0; i < chromosome.length; i++) {
            int workerIdx = chromosome[i];
            priorityPenalty += jobs.get(i).getPriority() * projectedLoad[workerIdx];
        }

        return -(capacityPenalty) - (IMBALANCE_WEIGHT * variance) - (PRIORITY_WEIGHT * priorityPenalty);
    }

    private List<JobAssignmentPlan> toPlan(int[] chromosome, List<Job> jobs, List<Worker> workers) {
        int numWorkers = workers.size();
        int[] remainingCapacity = new int[numWorkers];
        for (int w = 0; w < numWorkers; w++) {
            remainingCapacity[w] = workers.get(w).getCapacity() - workers.get(w).getCurrentLoad();
        }

        List<JobAssignmentPlan> result = new ArrayList<>();
        for (int i = 0; i < chromosome.length; i++) {
            int workerIdx = chromosome[i];
            if (remainingCapacity[workerIdx] > 0) {
                result.add(new JobAssignmentPlan(jobs.get(i), workerIdx));
                remainingCapacity[workerIdx]--;
            }
        }
        return result;
    }
}
