import { AfterViewInit, Component, ElementRef, EventEmitter, HostListener, Input, OnInit, Output, QueryList, ViewChild, ViewChildren } from '@angular/core';
import { Player } from '../../models/Player';
import { CommonModule } from '@angular/common';
import { FormationService } from '../../services/formation.service';
import { PresetService } from '../../services/preset.service';


interface FieldArea {
  name: string;
  element: HTMLElement;
}


@Component({
  selector: 'app-field',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './field.component.html',
  styleUrl: './field.component.css'
})
export class FieldComponent implements OnInit{
    @Input() players: Player[] = [];
    

    selectedPlayer: Player | null = null;
    private isDragging = false;
    private originalCoords: Map<number, { top: string; left: string }> = new Map();
    private isInitialized = false;


    areaCounts: { [key: string]: number } = {};
    setorCounts = {
      defesa: 0,
      meio: 0, 
      ataque: 0
    };

    private areaGroups = {
      defesa: ['lateral-esquerdo', 'zagueiro', 'lateral-direito'],
      meio: ['meia-esquerda', 'meia-centro', 'meia-volante', 'meia-atacante', 'meia-direita'],
      ataque: ['ponta-esquerda', 'centroavante', 'ponta-direita']
    };


    constructor(private formationService: FormationService, private presetService: PresetService){}

    ngOnChanges(){
      this.formationService.setPlayers(this.players);
    }

  
    calculateSetorCounts() {
     
      this.setorCounts = { defesa: 0, meio: 0, ataque: 0 };
      
  
      Object.keys(this.areaCounts).forEach(area => {
        if (this.areaGroups.defesa.includes(area)) {
          this.setorCounts.defesa += this.areaCounts[area];
        } else if (this.areaGroups.meio.includes(area)) {
          this.setorCounts.meio += this.areaCounts[area];
        } else if (this.areaGroups.ataque.includes(area)) {
          this.setorCounts.ataque += this.areaCounts[area];
        }
      });
      
      return this.setorCounts;
    }

  
    private initializeAreaCounts() {
      Object.keys(this.positionMap).forEach(area => {
        this.areaCounts[area] = 0;
      });
    }

    ngOnInit() {
      this.initializePlayers();
      this.initializeAreaCounts();
      this.updateAreaCounts();
    }
    
    updateAreaCounts() {
      this.areaCounts = this.countPlayersByArea();
      this.calculateSetorCounts();
      const formationString = `${this.setorCounts.defesa}-${this.setorCounts.meio}-${this.setorCounts.ataque}`;
      this.formationService.updateFormation(formationString);
    }


    initializePlayers() {
      
      this.players.forEach(player => {
        if (player.coords) {
          this.originalCoords.set(player.id, { ...player.coords });
        }
      });
    }

    fieldAreas: FieldArea[] = [];
 

  private positionMap: { [key: string]: string } = {
    'ponta-esquerda': 'Ponta Esquerda',
    'centroavante': 'Centroavante', 
    'ponta-direita': 'Ponta Direita',
    'meia-esquerda': 'Meia Esquerda', 
    'meia-atacante': 'Meia Atacante',
    'meia-direita': 'Meia Direita',
    'lateral-esquerdo': 'Lateral Esquerdo',
    'meia-centro': 'Meia de Ligação',
    'lateral-direito': 'Lateral Direito',
    'meia-volante': 'Volante',
    'goleiro': 'Goleiro',
    'zagueiro': 'Zagueiro'
    
  };

  countPlayersByArea(): { [key: string]: number } {
    const areaCounts: { [key: string]: number } = {};
    
    
    Object.keys(this.positionMap).forEach(area => {
      areaCounts[area] = 0;
    });
  
    
    if (this.fieldAreas.length === 0) {
      return areaCounts;
    }
  
    
    this.players.forEach(player => {
      const area = this.detectAreaFromCoords(player.coords);
      if (area && areaCounts.hasOwnProperty(area)) {
        areaCounts[area]++;
      }
    });
  
    return areaCounts;
  }
  
  
  private detectAreaFromCoords(coords?: { top: string; left: string }): string | null {
    if (!coords) return null;
  
    const pitch = document.getElementById('pitch');
    if (!pitch) return null;
  
    const pitchRect = pitch.getBoundingClientRect();
    
   
    const x = (parseFloat(coords.left) / 100) * pitchRect.width;
    const y = (parseFloat(coords.top) / 100) * pitchRect.height;
    
   
    const relX = (x / pitchRect.width) * 100;
    const relY = (y / pitchRect.height) * 100;
  

    const goleiroArea = this.fieldAreas.find(area => area.name === 'goleiro');
    if (goleiroArea && this.isInsideArea(relX, relY, goleiroArea, pitchRect)) {
      return 'goleiro';
    }
  
   
    for (const area of this.fieldAreas) {
      if (area.name === 'goleiro') continue;
      
      if (this.isInsideArea(relX, relY, area, pitchRect)) {
        return area.name;
      }
    }
  
    return null;
  }
  


  ngAfterViewInit() {
    this.createFieldAreas();
  
    setTimeout(() => {
      this.isInitialized = true;
     
      this.updateAreaCounts();
    }, 100);
  }

  private createFieldAreas() {
    const pitch = document.getElementById('pitch');
    if (!pitch) return;
  
    
    this.fieldAreas = [
      this.createArea(pitch, 'ponta-esquerda', 0, 0, 25, 30),
      
      this.createArea(pitch, 'meia-esquerda', 0, 30, 25, 40),
      
      this.createArea(pitch, 'lateral-esquerdo', 0, 70, 25, 30),
    
      this.createArea(pitch, 'centroavante', 25, 0, 50, 20),
     
      this.createArea(pitch, 'meia-atacante', 25, 20, 50, 20),
      
      this.createArea(pitch, 'meia-centro', 25, 40, 50, 20),
      
      this.createArea(pitch, 'meia-volante', 25, 60, 50, 20),
      
      this.createArea(pitch, 'zagueiro', 25, 80, 50, 20),
      
      this.createArea(pitch, 'goleiro', 45, 92, 10, 8),
      
     
      
     
      this.createArea(pitch, 'ponta-direita', 75, 0, 25, 30),
     
      this.createArea(pitch, 'meia-direita', 75, 30, 25, 40),
    
      this.createArea(pitch, 'lateral-direito', 75, 70, 25, 30)
    ];
  }

  private createArea(pitch: HTMLElement, name: string, left: number, top: number, width: number, height: number): FieldArea {
    const areaDiv = document.createElement('div');
    areaDiv.className = 'field-area';
    areaDiv.style.position = 'absolute';
    areaDiv.style.left = `${left}%`;
    areaDiv.style.top = `${top}%`;
    areaDiv.style.width = `${width}%`;
    areaDiv.style.height = `${height}%`;
    areaDiv.style.pointerEvents = 'none'; 
    areaDiv.style.opacity = '0'; 
    areaDiv.style.zIndex = '1';
    
    pitch.appendChild(areaDiv);
    
    return { name, element: areaDiv };
  }

  onDragStart(event: DragEvent, player: Player) {
    this.isDragging = true;
    this.selectedPlayer = player;
    
    event.dataTransfer?.setData('text/plain', player.id.toString());
    event.dataTransfer!.effectAllowed = 'move';
    
    setTimeout(() => {
      const element = event.target as HTMLElement;
      element.classList.add('dragging');
    }, 0);
  }

  onDragEnd(event: DragEvent) {
    this.isDragging = false;
    const element = event.target as HTMLElement;
    element.classList.remove('dragging');

  }
  
  @HostListener('document:dragover', ['$event'])
  onDragOver(event: DragEvent) {
    event.preventDefault();
    
    if (event.dataTransfer) {
      event.dataTransfer.dropEffect = 'move';
    }
  }

  @HostListener('document:drop', ['$event'])
  onDrop(event: DragEvent) {
    event.preventDefault();
    
    if (!this.selectedPlayer) return;

    const pitch = document.getElementById('pitch');
    if (!pitch) return;

    const pitchRect = pitch.getBoundingClientRect();
    const x = event.clientX - pitchRect.left;
    const y = event.clientY - pitchRect.top;

    const maxX = pitchRect.width - 20;
    const maxY = pitchRect.height - 20;
    
    const newX = Math.max(20, Math.min(x, maxX));
    const newY = Math.max(20, Math.min(y, maxY));

    this.selectedPlayer.coords = {
      left: `${(newX / pitchRect.width) * 100}%`,
      top: `${(newY / pitchRect.height) * 100}%`
    };


    this.checkPlayerPosition(newX, newY, pitchRect);
    
    this.isDragging = false;
    this.updateAreaCounts();
  }

  private checkPlayerPosition(x: number, y: number, pitchRect: DOMRect) {
    if (!this.selectedPlayer) return;

    const relX = (x / pitchRect.width) * 100;
    const relY = (y / pitchRect.height) * 100;
  
    let detectedArea = null;
  

    const goleiroArea = this.fieldAreas.find(area => area.name === 'goleiro');
    if (goleiroArea && this.isInsideArea(relX, relY, goleiroArea, pitchRect)) {
      detectedArea = goleiroArea;
    } 
  
    else {
      for (const area of this.fieldAreas) {
        
        if (area.name === 'Goleiro') continue;
        
        if (this.isInsideArea(relX, relY, area, pitchRect)) {
          detectedArea = area;
          break; 
        }
      }
    }
  
    if (detectedArea && this.positionMap[detectedArea.name]) {
      this.selectedPlayer.position = detectedArea.name;
      this.selectedPlayer.name = this.positionMap[detectedArea.name];
    } else {
      this.selectedPlayer.position = "undefined";
      this.selectedPlayer.name = `Jogador ${this.selectedPlayer.id}`;
    }
  }

  selectPlayer(player: Player) {
    if (!this.isDragging) {
      this.selectedPlayer = player;
    }
  }



  private isInsideArea(relX: number, relY: number, area: FieldArea, pitchRect: DOMRect): boolean {
    const areaRect = area.element.getBoundingClientRect();
    
    const areaLeft = ((areaRect.left - pitchRect.left) / pitchRect.width) * 100;
    const areaTop = ((areaRect.top - pitchRect.top) / pitchRect.height) * 100;
    const areaRight = areaLeft + parseFloat(area.element.style.width);
    const areaBottom = areaTop + parseFloat(area.element.style.height);
  
    return relX >= areaLeft && relX <= areaRight && 
           relY >= areaTop && relY <= areaBottom;
  }

  // Método para debug: visualizar as áreas 
  toggleAreaVisibility() {
    const areas = document.querySelectorAll('.field-area');
    const colors = ['#ff000030', '#00ff0030', '#0000ff30', '#ffff0030', '#ff00ff30', '#00ffff30', '#ff800030', '#8000ff30', '#ff008030', '#80ff0030', '#0080ff30'];
    
    areas.forEach((area, index) => {
      const currentOpacity = (area as HTMLElement).style.opacity;
      (area as HTMLElement).style.opacity = currentOpacity === '0.5' ? '0' : '0.5';
      (area as HTMLElement).style.backgroundColor = currentOpacity === '0.5' ? 'transparent' : colors[index % colors.length];
      (area as HTMLElement).style.border = currentOpacity === '0.5' ? 'none' : '1px solid white';
    });
  }

  
  
}