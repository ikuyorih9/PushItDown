export interface HeaderButtons{
    text:string;
    routerLink:string;
};

export const HEADER_BUTTONS = [
{
    text:'↪️ Sair', 
    routerLink: '/logout',
}, 
{
    text:'📜 Histórico', 
    routerLink: '/historico',  
}];