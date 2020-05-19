package com.owldevs.evolutionsim.models;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Evolution {
    private Integer width;
    private Integer height;
    private Integer hungryStepsLimit;
    private Integer hungryStep;
    private Integer generationNumber;
    private Integer newGenerationSize;
    private Integer foodQuantity;
    private Integer deathDistance;
    private Integer leaderBoardsSize;

    private List<Cell> cellOnBoardList = new LinkedList<>();
    private List<Food> foodOnBoardList = new LinkedList<>();
    private List<Cell> topGenerationCellList = new ArrayList<>();
    private List<Cell> topForeverCellList = new ArrayList<>();

    private Graph graph;

    public Evolution(Integer width, Integer height) {
        this.width = width;
        this.height = height;

        generationNumber = 0;
        hungryStepsLimit = 300;
        hungryStep = hungryStepsLimit;

        newGenerationSize = 48;
        leaderBoardsSize = (newGenerationSize) / 6;
        foodQuantity = 140;
        deathDistance = 50;

        graph = new Graph(width, height);
    }

    public Boolean isTimeToEvolute() {
        return hungryStep >= hungryStepsLimit;
    }

    public void evolute() {
        topForeverCellList.forEach(cell -> System.out.print(cell.totalEaten.toString() + " "));
        System.out.println("");
        topGenerationCellList.forEach(cell -> System.out.print(cell.totalEaten.toString() + " "));
        System.out.println("");

        //NEW GENERATION
        List<Cell> newGenerationCells = new LinkedList<>();
        for (int i = 0; i <= newGenerationSize; i++) {
            Cell newCell = new Cell((int) (Math.random() * width / 2 + width / 4), (int) (Math.random() * height / 2 + height / 4));
            newGenerationCells.add(newCell);
        }

        for (int i = 0; i < leaderBoardsSize; i++) {
            int rand1 = (int) Math.round(Math.random() * (leaderBoardsSize - 1));
            int rand2 = (int) Math.round(Math.random() * (leaderBoardsSize - 1));
            if (topForeverCellList.size() - 1 < rand1 || topForeverCellList.size() - 1 < rand2) continue;
            newGenerationCells.get(i).nn = topForeverCellList.get(rand1).nn.clone();
            newGenerationCells.get(i).nn.crossover(topForeverCellList.get(rand2).nn);
        }
        for (int i = leaderBoardsSize; i < leaderBoardsSize * 2; i++) {
            int rand1 = (int) Math.round(Math.random() * (leaderBoardsSize - 1));
            int rand2 = (int) Math.round(Math.random() * (leaderBoardsSize - 1));
            if (topForeverCellList.size() - 1 < rand1 || topGenerationCellList.size() - 1 < rand2) continue;
            newGenerationCells.get(i).nn = topForeverCellList.get(rand1).nn.clone();
            newGenerationCells.get(i).nn.crossover(topGenerationCellList.get(rand2).nn);
        }
        for (int i = leaderBoardsSize * 2; i < leaderBoardsSize * 3; i++) {
            int rand1 = (int) Math.round(Math.random() * (leaderBoardsSize - 1));
            int rand2 = (int) Math.round(Math.random() * (leaderBoardsSize - 1));
            if (topGenerationCellList.size() - 1 < rand1 || topGenerationCellList.size() - 1 < rand2) continue;
            newGenerationCells.get(i).nn = topGenerationCellList.get(rand1).nn.clone();
            newGenerationCells.get(i).nn.crossover(topGenerationCellList.get(rand2).nn);
        }
        for (int i = leaderBoardsSize * 3; i < leaderBoardsSize * 4; i++) {
            int rand = (int) Math.round(Math.random() * (leaderBoardsSize - 1));
            if (topForeverCellList.size() - 1 < rand) continue;
            newGenerationCells.get(i).nn = topForeverCellList.get(rand).nn.clone();
            newGenerationCells.get(i).nn.mutate(0.05, 0.2);
        }
        for (int i = leaderBoardsSize * 4; i < leaderBoardsSize * 5; i++) {
            int rand = (int) Math.round(Math.random() * (leaderBoardsSize - 1));
            if (topGenerationCellList.size() - 1 < rand) continue;
            newGenerationCells.get(i).nn = topGenerationCellList.get(rand).nn.clone();
            newGenerationCells.get(i).nn.mutate(0.1, .5);
        }
        topGenerationCellList.clear();

        cellOnBoardList.clear();
        cellOnBoardList.addAll(newGenerationCells);

        //generate food
        foodOnBoardList.clear();
        for (int i = 0; i <= foodQuantity; i++) {
            foodOnBoardList.add(new Food((int) (Math.random() * width), (int) (Math.random() * height)));
        }

        hungryStep = 0;
        generationNumber++;
        System.out.println("New generation " + generationNumber.toString());

        //topForeverCellList.forEach(cell -> cell.totalEaten--);
    }

    public void calcStep() {
        List<Food> foodToRemoveList = new ArrayList<>();
        List<Cell> cellToBirthList = new ArrayList<>();
        List<Cell> cellToRemoveList = new ArrayList<>();

        cellOnBoardList.forEach(cell -> {
            Food nearFood = findNearFoodForCell(cell);
            if (nearFood != null) {
                cell.setNearFood(nearFood);
                cell.doStep();

                if (isCellOutOfBoard(cell)) {
                    cellToRemoveList.add(cell);

                } else if (cell.tryToEatNearFood()) {
                    hungryStep = 0;
                    tryToPutCellOnLeaderBoards(cell);
                    if (!foodToRemoveList.contains(nearFood)) {
                        foodToRemoveList.add(nearFood);
                    }

                    if (cell.canDiv()) {
                        cellToBirthList.add(cell.div(topForeverCellList.size() > 0 ? topForeverCellList.get(
                                (int)(Math.random() * topForeverCellList.size())
                        ) : null ));
                    }
                }
            }
        });

        if (foodToRemoveList.size() > 0) {
            for (Food food : foodToRemoveList) {
                foodOnBoardList.remove(food);
                foodOnBoardList.add(new Food((int) (Math.random() * width), (int) (Math.random() * height)));
            }
            foodToRemoveList.clear();
        }
        hungryStep++;

        cellOnBoardList.stream().filter(Cell::isDead).forEach(cellToRemoveList::add);

        cellOnBoardList.removeAll(cellToRemoveList);
        cellOnBoardList.addAll(cellToBirthList);

        graph.add(cellOnBoardList.size());

    }

    private Food findNearFoodForCell(Cell cell) {
        Food nearFood = null;
        Double distanceToFood = null;
        for (Food food : foodOnBoardList) {
            if (nearFood == null) {
                nearFood = food;
                distanceToFood = cell.distanceToFood(food);
                continue;
            }

            if (cell.distanceToFood(food) < distanceToFood) {
                nearFood = food;
                distanceToFood = cell.distanceToFood(food);
            }
        }
        return nearFood;
    }

    public List<Drawable> getSprites() {
        List<Drawable> sprites = new ArrayList<>();
        sprites.addAll(foodOnBoardList);
        sprites.addAll(cellOnBoardList);

        sprites.add(graph);
        return sprites;
    }

    private Boolean isCellOutOfBoard(Cell cell) {
        return cell.x < -deathDistance || cell.x > (width + deathDistance) ||
                (cell.y < -deathDistance) || (cell.y > (height + deathDistance));
    }

    private void tryToPutCellOnLeaderBoards(Cell cell) {
        topGenerationCellList = tryToPutCellOnLeaderBoard(cell, topGenerationCellList);
        topForeverCellList = tryToPutCellOnLeaderBoard(cell, topForeverCellList);
    }

    private List<Cell> tryToPutCellOnLeaderBoard(Cell cell, List<Cell> leaderBoard) {
        if (!leaderBoard.contains(cell)) {
            if (leaderBoard.size() < leaderBoardsSize) {
                leaderBoard.add(cell);
            } else if (leaderBoard.get(leaderBoard.size() - 1).totalEaten < cell.totalEaten) {
                leaderBoard.set(leaderBoard.size() - 1, cell);
            }
        }
        leaderBoard = leaderBoard.stream()
                .sorted(Comparator.comparingInt(Cell::getCount).reversed())
                .collect(Collectors.toList());
        return leaderBoard;
    }
}