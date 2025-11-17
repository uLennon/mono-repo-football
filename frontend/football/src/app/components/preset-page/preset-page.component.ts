import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { PresetService } from '../../services/preset.service';

@Component({
  selector: 'app-preset-page',
  standalone: true,
  imports: [],
  templateUrl: './preset-page.component.html',
  styleUrl: './preset-page.component.css'
})
export class PresetPageComponent implements OnInit{
  currentSet:number = 1;
  totalSet: number = 3;
  @Output() presetChange = new EventEmitter<number>();

  constructor(private presetService: PresetService){}

  ngOnInit(): void {
    this.currentSet = this.presetService.getCurrentPresetid();
  }

  changePreset(option: number){
    if(option >= 1 && option <= this.totalSet){
        this.currentSet = option;
        this.presetService.setCurrentPresetId(option);
        this.presetChange.emit(option); 
    }
  }
}
