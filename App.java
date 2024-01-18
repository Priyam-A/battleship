import java.util.*; //Primarily for Scanner class
public class App{
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter player 1 name: ");
        String name1 = sc.next();
        System.out.print("Enter player 2 name: ");
        String name2 =sc.next();
        BattleShip game = new BattleShip(name1,name2);
        game.play();
        sc.close();
    }
}
class player{ //Defining a player
    String name;
    char[][] playarea= new char[8][8];
    int[] shipLives = {2,3,4,5,6}; //number of hits each ship can take
    int sinkCount=0;                //Number of enemy ships sunk
    void setUp(){
        for (int i=1; i<6; i++){
            displayMe(playarea); //Displays playarea of player
            boolean valid;
            do{                 //This loop checks if the given input is valid
                valid=true;         //(ie whether the ships fit in the 8x8 gridspace without crossing each other)
                Scanner sc = new Scanner(System.in);
                System.out.print("Enter row for head(A-H): ");
                String inp =  sc.next();
                char rowName = inp.charAt(0);
                if (rowName<'A' || rowName > 'H'){
                    valid=false;
                    continue;
                }
                System.out.print("Enter col for head(A-H): ");
                char colName = sc.next().charAt(0);
                if (colName<'A' || colName > 'H'){
                    valid=false;
                    continue;
                }
                System.out.print("Enter orientation(R-Right, D-Down): ");
                char orient = sc.next().charAt(0);
                if (!(orient=='R' || orient == 'D')){
                    valid=false;
                    continue;
                }
                int row = (int)(rowName-'A');
                int col = (int)(colName-'A');
                if (orient=='R'){
                    if ((col+i)>7){
                        valid=false;
                        continue;
                    }else{
                        for(int j=0; j<i+1; j++){
                            if (playarea[row][col+j]!='\u0000'){
                                valid=false;
                                break;
                            }
                        }
                        if(valid){  //If found to be valid the ship is added to the playarea
                            for(int j=0; j<i+1; j++){
                                playarea[row][col+j]=(char)(96+i);
                            }
                        }else{
                            continue;
                        }
                    }
                } else {
                    if ((row+i)>7){
                        valid = false;
                        continue;
                    }else{
                        for(int j=0; j<i+1; j++){
                            if (playarea[row+j][col]!='\u0000'){
                                valid=false;
                                break;
                            }
                        }
                        if (valid){ //If found to be valid the ship is added to the playarea
                            for(int j=0;j<i+1;j++){
                                playarea[row+j][col]=(char)(96+i);
                            }
                        }else{
                            continue;
                        }                           
                    }
                }
            }while (valid==false);  //The input is taken until a valid input is found
        }
    }
    void displayMe(char[][] playarea){
        System.out.println("  A B C D E F G H ");
        for (int i=0;i<8;i++){
            System.out.print((char)(65+i));
            System.out.print(" ");
            for (int j=0;j<8;j++){
                char token = playarea[i][j];
                if (token=='\u0000'){
                    System.out.print("X ");
                }else{
                    System.out.print(token); //Here the status of all the tokens are shown, ie Hit(H), Miss(M) or unaccessed(Ship Code(a-h)) 
                    System.out.print(" ");
                }
            } 
            System.out.println();
        }  
    }
}
class BattleShip{ 
    player player1 = new player();
    player player2 = new player();
    BattleShip(String player1,String player2){ //The arguments are the names of the players
        this.player1.name=player1;
        this.player2.name=player2;
        this.player1.setUp();
        this.player2.setUp();
    }
    void displayEnemy(char[][] playarea){       
        System.out.println("  A B C D E F G H "); //Displays column numbers for reference
        for (int i=0;i<8;i++){              
            System.out.print((char)(65+i)); //Displays row numbers for reference
            System.out.print(" ");
            for (int j=0;j<8;j++){
                char token = playarea[i][j]; 
                if (token=='\u0000' || (playarea[i][j]>='a' && playarea[i][j]<='h')){ //if the point is not accessed, an X is shown
                    System.out.print("X ");
                }else{
                    System.out.print(token); //if a miss or a hit was made, an M or H is shown respectively
                    System.out.print(" ");
                }
            } 
            System.out.println();
        }
    }
    
    boolean checkValid(char name){ //Checks whether the input is valid or not
        if (name>='A' && name <='H'){
            return true;
        }
        return false;
    }
    void chance(player player1, player player2){ //player1's turn, player2 is the enemy
        Scanner sc = new Scanner(System.in);
        char row = ' ',col=' ';
        System.out.println(player1.name + "'s map"); 
        System.out.println();
        player1.displayMe(player1.playarea);
        System.out.println(player2.name+"'s map");
        System.out.println();
        displayEnemy(player2.playarea);
        do{
        System.out.print("Enter row: ");
        row = sc.next().charAt(0);
        }while(!checkValid(row));
        do{
        System.out.print("Enter col: ");
        col =sc.next().charAt(0);
        }while(!checkValid(col)); //checking validity of input taken
        int r=(int)(row-'A'), c=(int)(col-'A'); //explicitly converting char row names to int row values
        char val2=player2.playarea[r][c];
        if(val2 =='\u0000' || val2 == 'M'){
            System.out.println("Miss...");
            player2.playarea[r][c]='M';
        }else if(!(val2=='H')){
            System.out.println("Hit!!");
            player2.playarea[r][c]='H';
            int idx = (int)(val2 - 'a');
            player2.shipLives[idx]=player2.shipLives[idx]-1;
            if (player2.shipLives[idx]==0){
                System.out.println("sunk ship "+val2);
                player1.sinkCount++;
            }
        }else{
            System.out.println("Already Hit");
        }
        System.out.println();
    }
    boolean checkloss(player player1){
        /*for(int i=0;i<5;i++){
            if(player1.shipLives[i]!=0){
                return false;
            }
        }*/
        if (player1.sinkCount==5){
            return true;
        }
        return false;
    }
    void play(){
        while(true){
            if (!(checkloss(player1))){
                chance(player1, player2); //player1 goes first against player2
            }else{
                System.out.println("The winner is "+player2.name);
                break;
            }
            if(!checkloss(player2)){
                chance(player2, player1); //player2 goes first against player1;
            }else{
                System.out.println("The winner is"+player1.name);
                break;
            }
        }
    }
}

