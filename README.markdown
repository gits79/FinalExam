# 최적화 알고리즘을 이용한 회귀식 추정

### 개념 설명
* 독립변수 (independent variable) : 실험하는 사람에 의해 통제되어 독립적으로 주어지는 변수
* 종속변수 (dependent variable) : 독립변수와 오차에 의해 결정되는 변수

회귀 분석은 관찰된 연속형 변수들에 대해 두 변수 사이의 모형을 구한뒤 적합도를 측정해 내는 분석 방법입니다.\
일반적으로 그래프로 표현되는 이 방법은 종속 변수와 독립 변수 간의 관계를 테스트합니다. 일반적으로 독립 변수는 종속 변수에 따라 변경되며 회귀 분석은 해당 변경에서 가장 중요한 요소에 대한 답을 찾으려고 시도합니다.



<br/>

![img](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FdkY6Q0%2FbtqK7c9I5V6%2F7O0LzeFYakSWzlYVcKBLA0%2Fimg.png)

위의 그림은 회귀 모형중 가장 간단한 형태인 단순 선형회귀 모형입니다.\
회귀분석은 미지의 모수 절편 β0, 기울기 β1를 추정합니다.

![img](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FdxifDN%2FbtqK7cIC1R5%2FpdXFcP2hLI07fs3KNWgo91%2Fimg.png)

β0 +β1 xi는 xi일 때 yi의 평균을 의미합니다.

모수 추정
![img](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FbywcVL%2FbtqK7BuCtaB%2FhKYexuOGJV0ErJImG2KcIk%2Fimg.png)

 
β0,β1의 추정값은 b0,b1 로 표현합니다.


좀 더 나은 모수 값을 찾기 위해서는 적합도라는 개념이 등장합니다.


![img](https://mblogthumb-phinf.pstatic.net/MjAxODEyMjBfMTEy/MDAxNTQ1Mjk2NDYyMDQ1.QOVPdfhULPlMv4t8SekN8tt2HKGzh9Ys0i-FUPCHc1kg.k87kHgm4VajuPm9Zjv6Tsdd_9RtMyrs8WCnoHiLjvPcg.PNG.nilsine11202/20181220175216.png?type=w800)


임의의 데이터 점 A에서 이 지점에서 회귀식까지의 거리는 ei, 즉 잔차항이고

이 잔차항과 회귀 추정치 Y hat의 합은

Yi에서 Y bar를 뺀 거리와 동일하다는 것을 그림으로 이해할 수 있다.

이 사실을 바탕으로, 

양 항을 제곱하고 시그마를 취해주면 

2ab에 해당하는 부분은 정규방정식을 통해 0이라는 것을 알 수 있다.

따라서

다음과 같이 식을 정리할 수 있다.

![img](https://mblogthumb-phinf.pstatic.net/MjAxODEyMjBfODMg/MDAxNTQ1Mjk2NjM0NzEx.J43VFf__tOxNTaZ5zEWgHnTphXPTxgPolGA9iXTv2u8g.7HD9GVmaFrGQP4yzSk41OrFSjrmZOErFGjNL0ge4Rc8g.PNG.nilsine11202/20181220180346.png?type=w800)



## 2010~2020 연도별 최저임급 회귀식 추정


![img](https://t1.daumcdn.net/cfile/tistory/99FB11405DC1DE6C0A)

위의 데이터를 참고하여 회귀식 추정을 하려고 했으나 회귀 추정을 이해하기 어려웠으며 코드로 구현하려고 했을때 적합도 등 수식을 활용하기 어려워 더이상 진행할 수 없었습니다. 그래서 수업시간에 배운 유전 알고리즘에 대해 복습해보았습니다.



### 전체코드


```java
import java.util.ArrayList;
import java.util.Random;

public class Main {

    static class GenericAlgorithm{
        public double solve(int nCandidates, Problem p){
            int nGenerations = 100000;
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
                
            }

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
```



유전 알고리즘의 수도코드
```
1. 초기 후보해 집합 G0을 생성한다.
2. G0의 각 후보해를 평가한다.
3. t <- 0
4. repeat
5.  G(t)로부터 G(t+1)을 생성한다.
6. G(t+1)의 각 후보해를 평가한다.
7. t <- t+1
8. until(종료 조건이 만족될 때까지)
9. return G(t)의 후보해 중에서 가장 우수한 해
```



1. 선택연산

선택 연산은 현재 세대의 후보해 중에서 우수한 후보해를 선택하는 연산으로 이들 중 우수한 해는 중복되어 선택되는 '적자생존'의 개념을 모방했다.\
선택 연산을 구현하는 간단한 방법은 룰렛 휠이다.


![img](1.png)
0~31의 숫자중에서 랜덤하게 뽑아 4개의 후보해 23,4,18,24가 선택되었으며 적합도를 계산하였습니다.


![img](2.png)

원판은 실제로는 일렬로 쭉 펼처놓은 상태이며 0~1 구간으로 이루어져 있습니다.

![img](3.png)

룰렛 휠 연산후 23,4,18,24 였던 후보해가 모두 18으로 교체 된 것을 확인할 수 있습니다.

![img](4.png)

다양한 경우의 수를 보기 위해 다시 돌려보았더니 원래 후보해 (4,11,27,21) 에서
(4,21,11,21) 으로 후보해가 변경되었습니다.


2. 교차연산
2개의 후보해가 이진수로 표현될때 교차점 이후의 부분을 서로 교환하여 새로운 후보해가 만들어진다. 일반적으로 교차율은 0.2~1.0 범위로 한다.

![img](5.png)
교차 연산의 경우 홀수번째 후보해와 짝수번째 후보해를 매핑시켜서 진행했습니다.

![img](6.png)
첫번째 후보해 4가 정수형 배열안에 00100 으로 들어갔습니다.\
마찬가지로 후보해 11 이 정수형 배열애 01011으로 들어갔습니다.

![img](7.png)
현재 교차율에 따라 인덱스는 랜덤하게 4로 결정되어 4번위치 마지막위치부터 바뀌게되어 기존 4 00100은 ->  00101으로 5가 되었고\
기존 11 01011은 -> 01010으로 10이 되었습니다.

![img](8.png)
 위의 과정을 통해 선택연산후 후보해 (4,21,11,21)에서 (5,10,21,21)으로 후보해가 변경되었습니다.

3. 돌연변이 연산
돌연변이 연산은 아주 작은 확률로 후보해의 일부분을 임의로 변형시키는 것이다.\
돌연변이 연산으로 적합도가 낮아질 수도 있으나 다음세대 후보해와 교차연산을 함으로써\
이후 세대에서 더 나은 후보해를 얻기 위함이다.

 ![img](9.png)
 후보해 중 랜덤하게 하나를 뽑은뒤 이진수로 변환시켜줍니다\
 여기서는 5가 뽑혔으며 현재 00101 입니다.

 ![img](10.png)
 현재 돌연변이 연산이 돌연변이 율이 0.25보다 낮을때 실행되기에 0.71이 나와 돌연변이 연산이 실행되지 않았지만 만약 0.25보다 낮았다면\
 특정인덱스의 값을 반대로 바꾸어줍니다.

![img](11.png)
결과적으로 1세대후 후보해는 (5,11,21,21)이 나왔으며 이것을 원하는 세대수 만큼\
반복시켜 최적해를 찾아갑니다.


## 결과 화면
![img](12.png)