package com.packt.snake;

import java.util.List;
import java.util.ArrayList;

public class AstarAgent {
    private List<AstarMap> openList;
    private List<AstarMap> closeList;

    public AstarAgent(){
        openList = new ArrayList<AstarMap>();
        closeList = new ArrayList<AstarMap>();
    }

    public List<AstarMap> findPath(AstarMap start, AstarMap end) {
        AstarMap center = null;
        openList.add(start);
        double minF = 0.0;
        boolean Flag = false;
        List<AstarMap> path = new ArrayList<AstarMap>();
        while (!Flag) {
            if (openList.size() == 0) {
                continue;
            } else {
                center = openList.get(0);
                minF = center.getF();
                for (AstarMap iter : openList) {
                    if (iter.getF() < minF) {
                        minF = iter.getF();
                        center = iter;
                    }
                }
            }
            closeList.add(center);
            openList.remove(center);
            if (closeList.contains(end)) {
                break;
            }

            List<AstarMap> aroundList = findAround(center);
            choosePathElement(aroundList, center, end);
        }

        do {
            path.add(center);
            if(center.getPrevious()!=null){
                center = center.getPrevious();
            }
        }while(center.getPrevious()!=null);

        return path;
    }


    public List<AstarMap> findAround(AstarMap center) {
        List<AstarMap> aroundList = new ArrayList<AstarMap>();
        AstarMap cell = null;
        int x = center.getX();
        int y = center.getY();

        if(x + 1 < 50) {
            cell = new AstarMap(x+1, y);
            cell.setPrevious(center);
            aroundList.add(cell);

        }

        if(x - 1 >= 0) {
            cell = new AstarMap(x-1, y);
            cell.setPrevious(center);
            aroundList.add(cell);

        }

        if(y - 1 >= 0) {
            cell = new AstarMap(x, y-1);
            cell.setPrevious(center);
            aroundList.add(cell);

        }

        if(y - 1 <50) {
            cell = new AstarMap(x, y+1);
            cell.setPrevious(center);
            aroundList.add(cell);
        }

        return aroundList;
    }

    public void choosePathElement(List<AstarMap> arroundList, AstarMap center, AstarMap end){
        for(AstarMap element: arroundList) {
            if(closeList.contains(element)){
                continue;
            }
            if (!openList.contains(element)){
                element.setG(center.getG() + 1);
                element.setH(end);
                element.setF();
                openList.add(element);
            }else {
                if(center.getH() + 1 < center.getH()) {
                    center.setPrevious(center);
                    center.setG(center.getG() + 1);
                }
            }
        }

    }
    public void restart(){
        openList.removeAll(openList);
        closeList.removeAll(closeList);
    }



}


