export interface FoodData{

    name: string
    calories: number
    carbohydrates: number
    protein: number
    weight: number
    fat: number

    
}

export interface User{
    email: string
    password: string
}

export interface Meal{
    foodlist: FoodData[]
    category: string
    timestamp: Date
    meal_id?: string
    imageurl?: string
}