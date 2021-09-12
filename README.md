# kotlin-todo-MVVM
kotlin todo app using MVVM pattern.

--------------- `add` ------------------------- `complete` ------------------------ `modify` --------------------- 
<img src="https://user-images.githubusercontent.com/71416677/132951673-93ffef6f-4572-486b-9026-38565aba6a39.gif" width="250" height="400"/>
<img src="https://user-images.githubusercontent.com/71416677/132951700-502da9a8-b4b1-4270-9a4b-31c87a7be12a.gif" width="250" height="400"/>
<img src="https://user-images.githubusercontent.com/71416677/132951687-67ca9d63-3a2d-4ff8-bc56-cf5a2f7df270.gif" width="250" height="400"/>  




-----------`remove single` ----------------`remove completed` -------------------`remove all`  ------------------  
<img src="https://user-images.githubusercontent.com/71416677/132951712-ce404bd5-e908-4576-9fd5-cb99bd4ad070.gif" width="250" height="400"/>
<img src="https://user-images.githubusercontent.com/71416677/132951721-588188f2-55ff-49c9-9a70-3cb49dc608b9.gif" width="250" height="400"/>
<img src="https://user-images.githubusercontent.com/71416677/132951728-d6b23015-1362-40eb-b039-f774d5210733.gif" width="250" height="400"/>  

## Architecture Pattern
MVVM 

![final-architecture](https://user-images.githubusercontent.com/71416677/132950781-3b8c1373-825b-4685-a900-de84f4e5f062.png)  

# Details
* `add`    
EditText and add button  

* `complete`  
Click task  

* `remove`  
Single task  (Swipe Left)  
Completed task  
All task  

* `modify`  
Swipe Right  

* `keep task`  
Using Room (Local Database), all tasks keep until delete the app.  


## Used Libraries
* Koin
* Room
* Coroutine  

## Directory Tree 

`└───todo_mvvm<br>
       ├───adapter  
       │    └─TaskAdapter.kt  
       │  
       ├───data  
       │     ├────db  
       │     │     └─TaskDao.kt  
       │     │     └─TaskDatabase.kt  
       │     │  
       │     ├────entity  
       │     │      └─TaskEntity.kt  
       │     │  
       │     └────repository  
       │            └─TaskRepository.kt  
       │            └─TaskRepositoryImpl.kt  
       │  
       │  
       ├───di  
       │    └─AppModule.kt  
       │    └─ProvideDB.kt  
       │  
       │  
       ├───view  
       │     └─BaseActivity.kt  
       │     └─MainActivity.kt  
       │  
       │  
       ├───viewmodel  
       │     ├────state  
       │     │      └─TaskState.kt  
       │     │  
       │     │  
       │     ├────BaseViewModel.kt   
       │     └────MainViewModel.kt  
       │  
       └───TodoApplication.kt`  
