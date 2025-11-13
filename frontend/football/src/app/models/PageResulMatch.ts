import { ResultMatch } from "./ResultMach";

export interface PageResultMatch {
    content: ResultMatch[];   
    totalPages: number;      
    totalElements: number;    
    size: number;             
    number: number;           
  }