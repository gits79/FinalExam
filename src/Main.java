import java.util.ArrayList;
import java.util.Random;

public class Main {

    static class GenericAlgorithm{
        public double solve(int nCandidates, Problem p){
            int nGenerations = 10000;
            double[] candidates = new double[nCandidates];


            for(int i=0;i<nGenerations;i++){
                candidates = select(nCandidates,candidates,p);
                candidates = crossover(nCandidates,candidates);
                candidates = mutate(nCandidates,candidates);
            }


            double maxFitness = 0;
            double result = 0;
            for(int i=0;i<nCandidates;i++) {
                double x = candidates[i];
                if(p.fit(x)>= maxFitness) {
                    result = x;
                    maxFitness = p.fit(x);


                }
                System.out.println("후보해는 : "+result+", 적합도는 : "+maxFitness);
            }
            System.out.println("==============================================");
            System.out.println("최적해는 : "+result+", 적합도는 : "+maxFitness);

            return 0;
        }

        static int[] toBinary(double a){
            // 10진수를 2진수 스트링으로 변환
            String strA = String.format("%05d", Integer.parseInt(Integer.toBinaryString((int)a).toString()));
            int size = strA.length();
            int[] A = new int[size];
            for(int i=0;i<size;i++){
                // 각자리 해당숫자를 임의의 정수형 배열에 넣음
                A[i] = Integer.parseInt(Character.toString(strA.charAt(i)));
            }

            return A;
        }

        static ArrayList<Double> cross(int[] A,int[] B,double rate){
            int size = A.length;
            // 교차점 시작 인덱스 번호 결정
            int cRate = (int) (rate * size);
            int[] tempA = A.clone();
            int[] tempB = B.clone();
            //교차점 시작인덱스부터 A와 B의 비트를 0에서 1로 1에서 0으로 바꿈
            for (int i=cRate;i<size;i++){
                A[i] = tempB[i];
                B[i] = tempA[i];
            }

            String strA="";
            String strB="";
            for(int i=0;i<size;i++){
                strA += A[i];
                strB += B[i];
            }

            ArrayList<Double> results = new ArrayList<>();
            results.add((double)Integer.parseInt(strA, 2));
            results.add((double)Integer.parseInt(strB, 2));

            return results;
        }

        private double[] mutate(int nCandidates,double[] candidates) {
            Random random = new Random();
            int rand = random.nextInt(nCandidates-1);
            int[] tempArr = toBinary(candidates[rand]);


            int popSize = nCandidates;
            int Length = tempArr.length;

            double mRate = random.nextDouble();

            if(mRate<=0.25) {

                int index = (int) (mRate * Length);

                if (tempArr[index] == 0)
                    tempArr[index] = 1;
                else if (tempArr[index] == 1)
                    tempArr[index] = 0;

                String str = "";
                for (int i = 0; i < tempArr.length; i++)
                    str += tempArr[i];

                candidates[rand] = (double) Integer.parseInt(str, 2);
            }



            return candidates;
        }

        private double[] crossover(int nCandidates,double[] candidates) {
            // 교차율 설정
            // 0.2~1까지의 랜덤 범위
            Random random = new Random();
            double cRate = random.nextDouble()+0.2;
            if(cRate>=1)
                cRate = 1.0;

            ArrayList<Double> resultCandidates = new ArrayList<>();
            for (int i=0;i<nCandidates;i++){
                if (i+2>=nCandidates)
                    break;
                // 1,3 과 2,4 홀수 짝수번째 매핑
                int[] tempA = toBinary(candidates[i]);
                int[] tempB = toBinary(candidates[i+2]);

                ArrayList<Double> tempCandidates = new ArrayList<>();
                tempCandidates = cross(tempA,tempB,cRate);

                resultCandidates.add(tempCandidates.get(0));
                resultCandidates.add(tempCandidates.get(1));

            }

            for (int i=0;i<nCandidates;i++){
                candidates[i] = resultCandidates.get(i);
            }






            return candidates;
        }

        private double[] select(int nCandidates,double[] candidates,Problem p) {


            // 0~31 까지 랜덤 후보해 선택
            Random random = new Random();
            // 4개의 후보해 선택
            for (int i=0;i<nCandidates;i++){
                candidates[i] = random.nextInt(31);
            }


            double sum = 0;
            double average = 0;
            double[] fitness = new double[nCandidates];

            // 후보해 적합도 계산한 값 넣고 후보해 총합 계산
            for (int i=0;i<nCandidates;i++){
                fitness[i] = p.fit(candidates[i]);
                sum += p.fit(candidates[i]);

            }

            average = sum /4;

            // 원판 생성
            // 실제로는 0~1 까지 일렬로 펼쳐 놓은 것
            double[] circle = new double[nCandidates];
            for (int i=0;i<nCandidates;i++){
                if(i==0)
                    circle[i] = fitness[i]/sum;
                else
                    circle[i] = circle[i-1] + fitness[i]/sum;
            }



            ArrayList<Integer> pickAnswer = new ArrayList<Integer>();
            for (int i=0;i<nCandidates;i++){
                // 0~1 난수 생성후 원판돌리기
                double pick = random.nextDouble();
                for(int j=0;j<nCandidates;j++) {
                    if (pick <= circle[j]) {
                        pickAnswer.add(j);
                        break;
                    }
                }

            }


            // 선택 연산후
            // 원판에서 나온 후보해들로 후보해 변경
            double[] tempCandidates = new double[nCandidates];
            tempCandidates = candidates.clone();

            for(int i=0;i<pickAnswer.size();i++){
                candidates[i] = tempCandidates[pickAnswer.get(i)];
            }







            return candidates;
        }


    }

    public static void main(String[] args) {
        GenericAlgorithm ga = new GenericAlgorithm();
        ga.solve(4, new Problem() {
            @Override
            public double fit(double x) {
                return -x*x + 38 * x + 80;
            }

        });



    }

}
